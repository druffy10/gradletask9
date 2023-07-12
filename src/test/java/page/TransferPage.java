package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement transferField = $(byText("Пополнение карты"));
    private final SelenideElement transferAmountField = $("[data-test-id=amount] input");
    private final SelenideElement fromCard = $("[data-test-id=from] input");
    private final SelenideElement buttonTransfer = $("[data-test-id=action-transfer]");
    private final SelenideElement error = $("[data-test-id=error-notification]");

    public TransferPage() {
        transferField.shouldBe(visible);
    }

    public DashboardPage validTransfer(String transferAmount, DataHelper.CardInfo cardInfo) {
        makeTransfer(transferAmount, cardInfo);
        return new DashboardPage();
    }

    public void makeTransfer(String transferAmount, DataHelper.CardInfo cardInfo) {
        transferAmountField.setValue(transferAmount);
        fromCard.setValue(cardInfo.getCardNumber());
        buttonTransfer.click();
    }

    public void errorMessage(String expectedText) {
        error.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
