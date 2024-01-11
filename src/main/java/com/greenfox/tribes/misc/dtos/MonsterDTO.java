package com.greenfox.tribes.misc.dtos;

import lombok.Data;

@Data
public class MonsterDTO {
  private Long id;
  private String name;
  private int hp;
  private int atk;
  private int dmg;
  private int def;
  private int lck;
  private int pullRing;
}
