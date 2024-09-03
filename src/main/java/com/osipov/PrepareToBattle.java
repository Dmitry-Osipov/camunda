package com.osipov;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class PrepareToBattle implements JavaDelegate {
    private static final Random random = new Random();
    @Value("${warriors.max}")
    private int maxWarriors;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var userWarriors = (int) delegateExecution.getVariable("userWarriors"); // Получаем данные от пользователя
        maxWarriors = maxWarriors <= 0 ? 100 : maxWarriors;

        if (userWarriors < 1 || userWarriors > maxWarriors) {
            throw new BpmnError("warriorsError");
        }

        var enemyWarriors = random.nextInt(100);
        List<Boolean> userArmy = new ArrayList<>(Collections.nCopies(userWarriors, true));
        System.out.println("Prepare to battle! Enemy army = " + enemyWarriors + " vs our army: " + userWarriors);
        delegateExecution.setVariable("userArmy", userArmy);
        delegateExecution.setVariable("enemyWarriors", enemyWarriors);
    }
}
