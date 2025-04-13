package com.financeTracker.controller;

import com.audit.annotation.Auditable;
import com.audit.model.EventType;
import com.financeTracker.annotation.Secured;
import com.financeTracker.dto.transaction.TransactionResponseDto;
import com.financeTracker.dto.user.TargetUserDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.TransactionService;
import com.financeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.audit.model.EventType.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Администрирование",
        description = "Операции администратора: управление пользователями, получение их транзакций")
@Validated
public class AdminRestController {

    private final UserService userService;
    private final TransactionService transactionService;

    @Operation(summary = "Получить список всех пользователей")
    @Secured(hasRole = "ADMIN")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.showAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Получить транзакции пользователя по ID")
    @Secured(hasRole = "ADMIN")
    @PostMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getUserTransactions(
            @Valid @RequestBody TargetUserDto targetUserDto) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByUserId(targetUserDto.getUserId());
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Заблокировать пользователя")
    @Secured(hasRole = "ADMIN")
    @Auditable(eventType = BLOCK_ACCOUNT)
    @PostMapping("/block")
    public ResponseEntity<Void> blockUser(
            @Valid @RequestBody TargetUserDto targetUserDto) {
        userService.blockedUserById(targetUserDto.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Разблокировать пользователя")
    @Secured(hasRole = "ADMIN")
    @Auditable(eventType = UNBLOCK_ACCOUNT)
    @PostMapping("/unblock")
    public ResponseEntity<Void> unblockUser(
            @Valid @RequestBody TargetUserDto targetUserDto) {
        userService.unblockedUserById(targetUserDto.getUserId());
        return ResponseEntity.ok().build();
    }
}
