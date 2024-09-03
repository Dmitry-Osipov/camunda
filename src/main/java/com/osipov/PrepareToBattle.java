package com.osipov;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PrepareToBattle implements JavaDelegate {
    private static final Random random = new Random();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var userWarriors = (int) delegateExecution.getVariable("userWarriors"); // Получаем данные от пользователя
        if (userWarriors < 1 || userWarriors > 100) {
            throw new BpmnError("warriorsError");
        }

        var enemyWarriors = random.nextInt(100);
        boolean isWin = false;
        String battleStatus;

        if (userWarriors - enemyWarriors > 0 ) {
            isWin = true;
            battleStatus = "Victory!";
        } else {
            battleStatus = "Fail! :(";
        }

        delegateExecution.setVariable("enemyWarriors", enemyWarriors);
        delegateExecution.setVariable("battleStatus", battleStatus);
        delegateExecution.setVariable("isWin", isWin);
    }
}
