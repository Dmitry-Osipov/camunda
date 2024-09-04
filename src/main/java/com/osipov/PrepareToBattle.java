package com.osipov;

import com.osipov.domain.Warrior;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.Spin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class PrepareToBattle implements JavaDelegate {
    private static final Random random = new Random();

    @Value("${warriors.max}")
    private int maxWarriors;

    @Value("${faker.url}")
    private String url;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var userWarriors = (int) delegateExecution.getVariable("userWarriors"); // Получаем данные от пользователя
        maxWarriors = maxWarriors <= 0 ? 100 : maxWarriors;
        if (userWarriors < 1 || userWarriors > maxWarriors) {
            throw new BpmnError("warriorsError");
        }

        List<Warrior> userArmy = new ArrayList<>();
        for (int i = 0; i < userWarriors; i++) {
            userArmy.add(createWarrior());
        }

        var enemyWarriors = random.nextInt(100);
        System.out.println("Prepare to battle! Enemy army = " + enemyWarriors + " vs our army: " + userWarriors);

        ObjectValue userArmyJson = Variables.objectValue(userArmy)
                .serializationDataFormat("application/json")  // Кастомная сериализация, можно сделать через property
                .create();
        delegateExecution.setVariable("userArmyJson", userArmyJson);
        delegateExecution.setVariable("userArmy", userArmy);
        delegateExecution.setVariable("enemyWarriors", enemyWarriors);
    }

    private Warrior createWarrior() {
        Map<String, String> headers = Map.of("Content-Type", "application/json");
        HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
        HttpRequest request = httpConnector.createRequest()
                .get()
                .url(url);
        request.setRequestParameter("headers", headers);

        HttpResponse response = request.execute();
        var warrior = new Warrior();
        if (response.getStatusCode() == 201) {
            // Вручную проставляем поля
//            SpinJsonNode node = Spin.JSON(response.getResponse());
//            warrior.setName(node.prop("name").stringValue());
//            warrior.setTitle(node.prop("title").stringValue());
//            warrior.setHp(node.prop("number").numberValue().intValue());
//            warrior.setIsAlive(true);
            // То же самое, но автоматически
            warrior = Spin
                    .JSON(response.getResponse())
                    .mapTo(Warrior.class);
            warrior.setIsAlive(true);
        }
        response.close();

        return warrior;
    }
}
