package com.analysts.encryptor.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class ApplicationTest {

    @Test
    public void testDecryption(){
        String password = "testPassword";
        String encryptedString = Application.printEncryptedStrings(password, "SourceString", false);
        System.out.println("Encrypted Source : " +encryptedString);
        String decryptedString = Application.printEncryptedStrings(password, encryptedString, true);
        System.out.println("Decrypted Source : " +decryptedString);

        assertEquals("SourceString", decryptedString);
    }

    @Test
    public void testOptions_p(){
        Options options = Application.createOptions();
        Option pswdOpt = options.getOption("p");
        assertEquals(true, pswdOpt.isRequired());
        assertEquals(true, pswdOpt.hasArg());
        assertEquals("password", pswdOpt.getLongOpt());
    }

    @Test
    public void testOptions_s(){
        Options options = Application.createOptions();
        Option pswdOpt = options.getOption("s");

        assertEquals(true, pswdOpt.isRequired());
        assertEquals(true, pswdOpt.hasArg());
        assertEquals("source", pswdOpt.getLongOpt());
    }

    @Test
    public void testOptions_d(){
        Options options = Application.createOptions();
        Option pswdOpt = options.getOption("d");

        assertEquals(false, pswdOpt.isRequired());
        assertEquals(false, pswdOpt.hasArg());
        assertEquals("decrypt", pswdOpt.getLongOpt());
    }

    @Test
    public void test_parseCommandLineString(){
        Options options = Application.createOptions();
        String expectedPassword = "testPassword";
        String expectedSourceString = "SourceString";
        String[] args = {"-p", expectedPassword, "-s", expectedSourceString};
        CommandLine cmd = Application.createCommandLine(args, options, new DefaultParser());

        assertEquals(expectedPassword, Application.parseCommandLineString("p", cmd, options));
        assertEquals(expectedSourceString, Application.parseCommandLineString("s", cmd, options));
    }

    @Test
    public void test_createCommandLine(){
        Options options = Application.createOptions();
        String expectedPassword = "testPassword";
        String expectedSourceString = "SourceString";
        boolean expectedDecryptValue = true;
        String[] args = {"-d", "-p", "testPassword", "-s", "SourceString"};

        CommandLine cmd = Application.createCommandLine(args, options, new DefaultParser());

        assertEquals(expectedPassword, cmd.getOptionValue("p"));
        assertEquals(expectedSourceString, cmd.getOptionValue("s"));
        assertEquals(expectedDecryptValue, cmd.hasOption("d"));
    }

}