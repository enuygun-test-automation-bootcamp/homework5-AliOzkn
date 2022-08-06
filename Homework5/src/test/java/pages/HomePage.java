package pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.Data;
import org.openqa.selenium.support.PageFactory;
import testng.ContactManagerTestNG;

@Data
public class HomePage{

    public HomePage(){
        PageFactory.initElements(new AppiumFieldDecorator(ContactManagerTestNG.driver), this);
    }


    @AndroidFindBy(id = "addContactButton")
    private MobileElement addContactBtn;

    @AndroidFindBy(id = "showInvisible")
    private MobileElement invisibleBtn;

}
