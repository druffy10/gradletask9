package test;

import data.DataHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.*;

public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    public void beforeEach() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);

        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    @Order(1)
    public void shouldTransferMoneyFromFirstCardToSecondCard() {
        var firstCardInfo = DataHelper.getFirstCard();
        var secondCardInfo = DataHelper.getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var transferAmount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - transferAmount;
        var expectedSecondCardBalance = secondCardBalance + transferAmount;
        var transferPage = dashboardPage.selectCard(secondCardInfo);
        dashboardPage = transferPage.validTransfer(String.valueOf(transferAmount), firstCardInfo);

        Assertions.assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(0));
        Assertions.assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(1));
    }
    @Test
    @Order(2)
    public void shouldNotChangeBalanceOnInvalidTransfer() {
        var firstCardInfo = DataHelper.getFirstCard();
        var secondCardInfo = DataHelper.getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var transferAmount = DataHelper.generateInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCard(secondCardInfo);
        transferPage.makeTransfer(String.valueOf(transferAmount), firstCardInfo);
        transferPage.errorMessage("Ошибка!");

        Assertions.assertEquals(firstCardBalance, dashboardPage.getCardBalance(0));
        Assertions.assertEquals(secondCardBalance, dashboardPage.getCardBalance(1));
    }
}

