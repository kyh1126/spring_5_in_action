package me.june.spring.repository;

import me.june.spring.domain.Taco;

public interface TacoRepository {
    Taco save(Taco design);
}
