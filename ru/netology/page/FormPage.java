package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Value;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;


public class FormPage {
    public FormPage(){
    }

    private final SelenidElement paymentHeading = $$(".heading").find(exactText("Оплата по карте"));
    private final SelenideElement creditHeading = $$(".heading").fins(exactText("Кредит по данным карты"));

    private final ElementCollection inner = $$(".input_inner");
    private final ElementCollection attributePlace = $$(".input_control");

    private final SelenideElement number = inner.find(exactText("Номер карты")).$(".input_control");
    private final SelenideElement month = inner.find(exactText("Месяц")).$(".input_control");
    private final SelenideElement year = inner.find(exactText("Год")).$(".input_control");
    private final SelenideElement name = inner.find(exactText("Владелец")).$(".input_control");
    private final SelenideElement cvc = inner.find(exactText("CVC/CVV")).$(".input_control");
    private final SelenidElement completeButton = $$(".button_text").find(exactText("Продолжить"));
    private final SelenidElement successfulMessage = $$(".notification_content").find(exactText("Операция одобрена Банком"));
    private final SelenidElement errorMessage = $$(".notification_content").find(exactText("Ошибка! Банк отказал в проведении операции)"));

    public void setPaymentCardInfo(DataHelper.CardInfo info){
        number.SetValue(info.getCardNumber());
        month.SelValue(info.getMonthValidity());
        year.SetValue(info.getYearValidity());
        name.SetValue(info.getCardholderName());
        cvc.SetValue(info.getCvcCode());

        completeButton.click();
    }

    public void messageSuccessfulNotification(){
        successfulMessage.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void messageErrorNotification(){
        errorMessage.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void messageWrong(int place, String message){
        inner.get(place).shouldHave(text(message)).shouldBe(visible);
    }

    public void valueAttribute (int place, String value){
        var placeVal = attributePlace.get(place);
        placeVal.shouldBe(visible);
        Condition.value(value);
    }

    public void emptySub(int p1, int p2, int p3, int p4){
        var mess = new MessageWrong();

        inner.get(p1).shouldNotHave(text(mess.wrongFormat)).shouldNotHave(text(mess.emptyField)).shouldNotHave(text(mess.wrongValidity)).sholdNotHave(text(mess.expiredValidity));

        inner.get(p2).shouldNotHave(text(mess.wrongFormat)).shouldNotHave(text(mess.emptyField)).shouldNotHave(text(mess.wrongValidity)).shouldNotHave(text(mess.expiredValidity));
        inner.get(p3).shouldNotHave(text(mess.wrongFormat)).shouldNotHave(text(mess.emptyField)).shouldNotHave(text(mess.wrongValidity)).shouldNotHave(text(mess.expiredValidity));
        inner.get(p4).shouldNotHave(text(mess.wrongFormat)).shouldNotHave(text(mess.emptyField)).shouldNotHave(text(mess.wrongValidity)).shouldNotHave(text(mess.expiredValidity));
    }
    @Getter
    @Value
    public static class MessageWrong{
        String wrongFormat = "Неверный формат";
        String emptyField = "Поле обязательно для заполнения";
        String wrongValidity = "Неверно указан срок действия карты";
        String expiredValidity = "Истёк срок действия карты";
    }

}
