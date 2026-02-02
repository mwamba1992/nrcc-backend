package tz.go.roadsfund.nrcc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.go.roadsfund.nrcc.entity.OtpToken;
import tz.go.roadsfund.nrcc.exception.BadRequestException;
import tz.go.roadsfund.nrcc.repository.OtpTokenRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final SmsService smsService;

    @Value("${otp.expiry-minutes:5}")
    private int otpExpiryMinutes;

    @Value("${otp.max-attempts:3}")
    private int maxAttempts;

    @Value("${otp.rate-limit-per-hour:5}")
    private int rateLimitPerHour;

    private static final SecureRandom random = new SecureRandom();

    public void sendOtp(String phoneNumber) {
        // Normalize phone number
        phoneNumber = normalizePhoneNumber(phoneNumber);

        // Rate limiting - max 5 OTPs per hour
        long recentOtps = otpTokenRepository.countByPhoneNumberAndCreatedAtAfter(
                phoneNumber, LocalDateTime.now().minusHours(1));
        if (recentOtps >= rateLimitPerHour) {
            throw new BadRequestException("Too many OTP requests. Please try again later.");
        }

        // Delete any existing OTPs for this number
        otpTokenRepository.deleteByPhoneNumber(phoneNumber);

        // Generate 6-digit OTP
        String otpCode = generateOtp();

        // Save OTP token
        OtpToken otpToken = OtpToken.builder()
                .phoneNumber(phoneNumber)
                .otpCode(otpCode)
                .expiresAt(LocalDateTime.now().plusMinutes(otpExpiryMinutes))
                .build();
        otpTokenRepository.save(otpToken);

        // Send SMS
        String message = String.format("Your NRCC login code is: %s. Valid for %d minutes.", otpCode, otpExpiryMinutes);
        smsService.sendSms(phoneNumber, message);

        log.info("OTP sent to phone number: {}", maskPhoneNumber(phoneNumber));
    }

    public boolean verifyOtp(String phoneNumber, String otpCode) {
        phoneNumber = normalizePhoneNumber(phoneNumber);

        OtpToken otpToken = otpTokenRepository
                .findByPhoneNumberAndVerifiedFalseAndExpiresAtAfter(phoneNumber, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException("Invalid or expired OTP. Please request a new one."));

        // Check max attempts
        if (otpToken.getAttempts() >= maxAttempts) {
            otpTokenRepository.delete(otpToken);
            throw new BadRequestException("Maximum verification attempts exceeded. Please request a new OTP.");
        }

        // Verify OTP
        if (!otpToken.getOtpCode().equals(otpCode)) {
            otpToken.incrementAttempts();
            otpTokenRepository.save(otpToken);
            throw new BadRequestException("Invalid OTP code. " + (maxAttempts - otpToken.getAttempts()) + " attempts remaining.");
        }

        // Mark as verified and delete
        otpToken.setVerified(true);
        otpTokenRepository.save(otpToken);
        otpTokenRepository.delete(otpToken);

        log.info("OTP verified successfully for phone number: {}", maskPhoneNumber(phoneNumber));
        return true;
    }

    private String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private String normalizePhoneNumber(String phoneNumber) {
        // Remove spaces and dashes
        phoneNumber = phoneNumber.replaceAll("[\\s-]", "");

        // Convert 0xxx to +255xxx
        if (phoneNumber.startsWith("0")) {
            phoneNumber = "+255" + phoneNumber.substring(1);
        }

        // Ensure +255 prefix
        if (!phoneNumber.startsWith("+255")) {
            phoneNumber = "+255" + phoneNumber;
        }

        return phoneNumber;
    }

    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() > 6) {
            return phoneNumber.substring(0, 4) + "****" + phoneNumber.substring(phoneNumber.length() - 2);
        }
        return "****";
    }

    // Cleanup expired tokens (can be scheduled)
    public void cleanupExpiredTokens() {
        otpTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Cleaned up expired OTP tokens");
    }
}
