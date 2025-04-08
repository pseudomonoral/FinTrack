package com.financeTracker.controller;

import com.audit.Auditable;
import com.audit.EventType;
import com.financeTracker.annotation.Secured;
import com.financeTracker.dto.TargetUserDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.service.TransactionService;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;
    private final TransactionService transactionService;

    @Secured(hasRole = "ADMIN")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.showAll();
        return ResponseEntity.ok(users);
    }

    @Secured(hasRole = "ADMIN")
    @PostMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getUserTransactions(@RequestBody TargetUserDto targetUserDto) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByUserId(targetUserDto.getUserId());
        return ResponseEntity.ok(transactions);
    }

    @Secured(hasRole = "ADMIN")
    @Auditable(eventType = EventType.BLOCK_ACCOUNT)
    @PostMapping("/block")
    public ResponseEntity<Void> blockUser(@RequestBody TargetUserDto targetUserDto) {
        userService.blockedUserById(targetUserDto.getUserId());
        return ResponseEntity.ok().build();
    }

    @Secured(hasRole = "ADMIN")
    @Auditable(eventType = EventType.UNBLOCK_ACCOUNT)
    @PostMapping("/unblock")
    public ResponseEntity<Void> unblockUser(@RequestBody TargetUserDto targetUserDto) {
        userService.unblockedUserById(targetUserDto.getUserId());
        return ResponseEntity.ok().build();
    }
}