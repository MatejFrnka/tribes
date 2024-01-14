package com.greenfox.tribes.game.controllers;

import com.greenfox.tribes.gameitems.models.Equipment;
import com.greenfox.tribes.gameitems.services.EquipmentService;
import com.greenfox.tribes.gameuser.models.WastelandUser;
import com.greenfox.tribes.gameuser.repositories.UserRepository;
import com.greenfox.tribes.gameuser.services.CustomUserDetailService;
import com.greenfox.tribes.misc.repositories.CharacterEquipmentRepo;
import com.greenfox.tribes.persona.dtos.PersonaDTO;
import com.greenfox.tribes.persona.models.Persona;
import com.greenfox.tribes.persona.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/character")
public class CharacterUI {

  @Autowired CharacterService characterService;
  @Autowired CustomUserDetailService userService;
  @Autowired UserRepository userRepository;

  @Autowired EquipmentService equipmentService;
  @Autowired CharacterEquipmentRepo pairingRepo;

  @GetMapping("/new")
  public String newCharacter() {
    return "persona-sites/character-creation";
  }

  @GetMapping("/new/create")
  public String finishCreation(
      @RequestParam("characterName") String characterName,
      @RequestParam("faction") String faction,
      @RequestParam("atk") int atk,
      @RequestParam("dmg") int dmg,
      @RequestParam("def") int def,
      @RequestParam("hp") int hp,
      @RequestParam("lck") int lck) {

    Persona persona =
        characterService.addCharacter(characterName, hp, atk, dmg, def, lck, faction, 100);
    System.out.println(persona);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    WastelandUser user = userRepository.findByUsername(auth.getName()).get();
    user.setPersona(persona);
    userRepository.save(user);

    return "redirect:/character/me";
  }

  @GetMapping("/me")
  public String myCharacter(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    WastelandUser user = userRepository.findByUsername(auth.getName()).get();

    if (user.getPersona() == null) {
      return "persona-sites/character-creation";
    } else {
      PersonaDTO dto = characterService.readCharacter();
      model.addAttribute("DTO", dto);
      int atkBonus = 0;
      int defBonus = 0;
      int hpBonus = 0;
      int lckBonus = 0;
      int dmgBonus = 0;

      if (dto.getEquipedItems() != null) {

        for (Equipment e : dto.getEquipedItems()) {
          atkBonus += e.getAtkBonus();
          defBonus += e.getDefBonus();
          hpBonus += e.getHpBonus();
          lckBonus += e.getLckBonus();
          dmgBonus += e.getDmgBonus();
        }
      }
      model.addAttribute("atkBonus", atkBonus);
      model.addAttribute("defBonus", defBonus);
      model.addAttribute("hpBonus", hpBonus);
      model.addAttribute("lckBonus", lckBonus);
      model.addAttribute("dmgBonus", dmgBonus);

      return "persona-sites/main-page";
    }
  }

  @RequestMapping("/me/equip")
  public String toggleEquip(@RequestParam("id") long id) {
    characterService.toggleEquip((long) id);
    return "redirect:/character/me";
  }
}
