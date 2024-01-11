package com.greenfox.tribes.persona.services;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.misc.models.CharacterEquipment;
import com.greenfox.tribes.persona.models.Persona;
import com.greenfox.tribes.persona.dtos.PersonaDTO;
import com.greenfox.tribes.persona.repositories.PersonaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CharacterService {

  @Autowired PersonaRepo playerCharacters;

  public void addCharacter(PersonaDTO dto) {
    Persona character = new Persona();
    character.setCharacterName(dto.getCharacterName());
    character.setHp(dto.getHp());
    character.setAtk(dto.getAtk());
    character.setDmg(dto.getDmg());
    character.setDef(dto.getDef());
    character.setLck(dto.getLck());
    character.setFaction(dto.getFaction());
    character.setPullRing(dto.getPullRing());
    playerCharacters.save(character);
  }

  public Persona addCharacter(
      String name, int hp, int atk, int dmg, int def, int lck, String faction, int gold) {
    Persona character = new Persona();
    character.setCharacterName(name);
    character.setHp(hp);
    character.setAtk(atk);
    character.setDmg(dmg);
    character.setDef(def);
    character.setLck(lck);
    character.setFaction(faction);
    character.setPullRing(gold);
    playerCharacters.save(character);
    return character;
  }

  public PersonaDTO readCharacter(Long id) {
    Persona character = playerCharacters.findById(id).get();
    PersonaDTO dto = new PersonaDTO();
    dto.setId(character.getId());
    dto.setCharacterName(character.getCharacterName());
    dto.setFaction(character.getFaction());
    dto.setAtk(character.getAtk());
    dto.setDmg(character.getDmg());
    dto.setDef(character.getDef());
    dto.setLck(character.getLck());
    dto.setHp(character.getHp());
    dto.setPullRing(character.getPullRing());

    return dto;
  }

  public PersonaDTO readCharacter() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Optional<Persona> loggedCharacter =
        playerCharacters.findPersonaByPlayer_Username(auth.getName());
    PersonaDTO dto = new PersonaDTO();
    if (loggedCharacter.isPresent()) {
      dto.setId(loggedCharacter.get().getId());
      dto.setCharacterName(loggedCharacter.get().getCharacterName());
      dto.setFaction(loggedCharacter.get().getFaction());
      dto.setAtk(loggedCharacter.get().getAtk());
      dto.setDmg(loggedCharacter.get().getDmg());
      dto.setDef(loggedCharacter.get().getDef());
      dto.setLck(loggedCharacter.get().getLck());
      dto.setHp(loggedCharacter.get().getHp());
      dto.setInventory(loggedCharacter.get().getInventory());
      System.out.println(dto.getInventory());
      dto.setPullRing(loggedCharacter.get().getPullRing());
    }

    // dto.setInventory(character.getInventory());

    return dto;
  }

  public void updateCharacter(PersonaDTO dto) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Optional<Persona> loggedCharacter =
        playerCharacters.findPersonaByPlayer_Username(auth.getName());
    if (loggedCharacter.isPresent()) {
      loggedCharacter.get().setAtk(dto.getAtk());
      loggedCharacter.get().setDmg(dto.getDmg());
      loggedCharacter.get().setDef(dto.getDef());
      loggedCharacter.get().setLck(dto.getLck());
      loggedCharacter.get().setPullRing(dto.getPullRing());
      playerCharacters.save(loggedCharacter.get());
    }
  }

  public Persona returnCharacter(Long id) {
    return playerCharacters.findById(id).get();
  }

  public Boolean isBusy(Long id) {
    try {
      return playerCharacters.findById(id).get().getIsBusy();
    } catch (Exception e) {
      throw new IllegalArgumentException("No such persona");
    }
  }

  public void toggleEquip(Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Optional<Persona> loggedCharacter =
        playerCharacters.findPersonaByPlayer_Username(auth.getName());
    CharacterEquipment equipment = null;
    for (CharacterEquipment e : loggedCharacter.get().getInventory()) {
      if (Objects.equals(e.getId(), id)) {
        equipment = e;
      }
    }

    if (equipment != null) {
      equipment.setIsEquipped(!equipment.getIsEquipped());
    }
  }

  public Boolean canBeEquipped(String type) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Optional<Persona> loggedCharacter =
        playerCharacters.findPersonaByPlayer_Username(auth.getName());

    List<Equipment> equipped = new ArrayList<>();
    if (loggedCharacter.isPresent()) {
      for (CharacterEquipment e : loggedCharacter.get().getInventory()) {
        Equipment equipment = e.getEquipment();
        if (e.getIsEquipped()) {
          if(Objects.equals(equipment.getType(), type)) {
            return false;
          }
        }
      }
    }
    return true;


  }


}
