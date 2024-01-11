package com.greenfox.tribes.misc.services;

import com.greenfox.tribes.misc.dtos.MonsterDTO;
import com.greenfox.tribes.misc.models.Monster;
import com.greenfox.tribes.misc.repositories.MonsterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonsterService {
  @Autowired MonsterRepo monsterRepo;

  public void createMonster(String name, int hp, int atk, int dmg, int def, int lck, int gold) {
    Monster monster = new Monster();
    monster.setAtk(atk);
    monster.setDmg(dmg);
    monster.setDef(def);
    monster.setLck(lck);
    monster.setHp(hp);
    monster.setPullRing(gold);
    monster.setName(name);
    monsterRepo.save(monster);
  }

  public MonsterDTO findMonster(long id) {
    Monster monster = monsterRepo.findById(id).get();
    MonsterDTO dto = new MonsterDTO();
    dto.setId(monster.getId());
    dto.setName(monster.getName());
    dto.setAtk(monster.getAtk());
    dto.setDmg(monster.getDmg());
    dto.setDef(monster.getDef());
    dto.setLck(monster.getLck());
    dto.setHp(monster.getHp());
    dto.setPullRing(monster.getPullRing());
    return dto;
  }
}
