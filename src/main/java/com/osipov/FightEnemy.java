package com.osipov;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class FightEnemy implements JavaDelegate {
    private static final Random random = new Random();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<Boolean> userArmy = (ArrayList<Boolean>) delegateExecution.getVariable("userArmy");
        int enemyWarriors = (int) delegateExecution.getVariable("enemyWarriors");
        Thread.sleep(2000);

        if (random.nextBoolean()) {
            enemyWarriors--;
            System.out.println("Enemy warrior killed!");
        } else {
            userArmy.remove(userArmy.size() - 1);
            System.out.println("Our warrior killed!");
        }

        delegateExecution.setVariable("enemyWarriors", enemyWarriors);
        delegateExecution.setVariable("userWarriors", userArmy.size());
        delegateExecution.setVariable("userArmy", userArmy);
    }
}
