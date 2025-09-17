/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.services;

import com.macnigor.cookmate.entity.AppState;
import com.macnigor.cookmate.repositories.AppStateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AppStateService {
    private final AppStateRepository appStateRepository;

    // Конструктор с внедрением зависимостей (репозиторий)
    public AppStateService(AppStateRepository appStateRepository) {
        this.appStateRepository = appStateRepository;
    }

    // Получение текущего состояния приложения из базы данных
    public Boolean getState() {
        log.debug("Попытка получения состояния приложения...");
        Optional<AppState> appState = appStateRepository.findById(1L);

        if (appState.isPresent()) {
            log.debug("Состояние приложения найдено: {}", appState.get().getIsDataLoaded());
            return appState.get().getIsDataLoaded();
        } else {
            log.warn("Состояние приложения не найдено, возвращается значение по умолчанию: false");
            return false;  // Если состояние не найдено, возвращаем false
        }
    }

    // Изменение состояния приложения (например, после загрузки данных)
    public void changedState(boolean flag) {
        log.debug("Попытка изменить состояние приложения на: {}", flag);

        Optional<AppState> appStateOptional = appStateRepository.findById(1L);

        if (appStateOptional.isPresent()) {
            AppState state = appStateOptional.get();
            log.info("Изменение состояния приложения с {} на {}", state.getIsDataLoaded(), flag);
            state.setIsDataLoaded(flag);  // Обновляем значение состояния
            appStateRepository.save(state); // Сохраняем изменения в базе данных
            log.info("Состояние приложения успешно обновлено на: {}", flag);
        } else {
            log.warn("Состояние приложения не найдено, невозможно обновить.");
        }
    }
}
