package com.greenfox.tribes.dtos;

import com.greenfox.tribes.models.Equipment;
import com.greenfox.tribes.models.CharacterEquipment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonaDTO extends CombatantDTO {

  private Long id;

  private String characterName;
  private String faction;

  private int atk; // chance to hit
  private int def; // chance to not be hit
  private int dmg; // how much damage is dealt in case of hit
  private int lck; // chance to crit, i.e. deal double damage
  private int hp;

  private int pullRing;

  private List<Equipment> inventory = new ArrayList<>();
  private List<Equipment> equipedItems = new ArrayList<>();

  public void setInventory(List<CharacterEquipment> bundle) {
    for (CharacterEquipment e : bundle) {
      Equipment equipment = e.getEquipment();

      inventory.add(equipment);
    }
  }

  public void setEquipedItems(List<CharacterEquipment> bundle) {
    for (CharacterEquipment e : bundle) {
      if (e.getIsEquipped()) {
        Equipment equipment = e.getEquipment();
        equipedItems.add(equipment);
      }
    }
  }
}
