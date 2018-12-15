package MLP.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

/**
 * author: ElinaValieva on 15.12.2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FileManagerServiceTest {

    @Autowired
    private FileManagerService fileManagerService;

    @Test
    public void createFile() throws IOException {
        File file = fileManagerService.createFile("1.png");
        Assert.assertNotNull(file);
    }

    @Test
    public void test() {
        Assert.assertTrue(1 + 1 == 2);
    }
}