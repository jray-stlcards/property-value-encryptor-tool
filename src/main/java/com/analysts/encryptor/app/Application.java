package com.analysts.encryptor.app;

import org.apache.commons.cli.*;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Application {
    public static void main(String...args) {
    	final Options options = createOptions();
		final CommandLine cmd = createCommandLine(args, options, new DefaultParser());
        final String password = parseCommandLineString("p", cmd, options);
        final String source = parseCommandLineString("s", cmd, options);
        boolean decrypt = cmd.hasOption("d");

        System.out.println("source: [" + source + "].");
        final String result = printEncryptedStrings(password, source, decrypt);
        System.out.println("result: [" + result + "].");
    }

	public static CommandLine createCommandLine(String[] args, Options options, CommandLineParser parser){
		try {
			return parser.parse(options, args);
		} catch (ParseException e) {
            printErrorMsg(e, options);
			return null;
		}
	}

	public static Options createOptions(){
		final Options options = new Options();
		final Option pswdOpt = new Option("p", "password", true, "encryptor password");
		pswdOpt.setRequired(true);
		options.addOption(pswdOpt);
		final Option srcOpt = new Option("s", "source", true, "source string");
		srcOpt.setRequired(true);
		options.addOption(srcOpt);
		options.addOption("d", "decrypt", false, "decrypt instead of encrypt");
		return options;
	}

	public static String parseCommandLineString(String strOption, CommandLine cmd, Options options){
		try{
			final String result = cmd.getOptionValue(strOption);
			checkArgument(isNotBlank(result), strOption.equalsIgnoreCase("p") ? "password is required" : strOption.equalsIgnoreCase("s") ? "source is required" : "");
			return result;
		} catch (IllegalArgumentException e) {
		    printErrorMsg(e, options);
			return null;
		}
	}
    public static void printErrorMsg(Exception e, Options options){
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.printHelp("java -jar property-value-encryptor-tool.jar [-d] -p <password> -s <source-string>", options);
        System.out.println(e.getMessage());
        System.exit(1);
    }

	public static String printEncryptedStrings(String password, String source, boolean decrypt){
		final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
		encryptor.setPassword(password);
        return decrypt ? encryptor.decrypt(source) : encryptor.encrypt(source);
	}
}
