package com.osipov;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class EvaluateBattle implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var userWarriors = (int) delegateExecution.getVariable("userWarriors");
        var enemyWarriors = (int) delegateExecution.getVariable("enemyWarriors");
        var isWin = false;
        String battleStatus;

        if (userWarriors > enemyWarriors) {
            isWin = true;
            battleStatus = "WIN";
        } else {
            battleStatus = "LOSE";
        }

        delegateExecution.setVariable("battleStatus", battleStatus);
        delegateExecution.setVariable("isWin", isWin);
        delegateExecution.setVariable("userWarriors", userWarriors);
        delegateExecution.setVariable("enemyWarriors", enemyWarriors);
    }
}
