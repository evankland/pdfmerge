package org.kirkland.pdfmerge;

import org.apache.commons.cli.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by evan on 3/10/2017.
 */
public class PdfMergeMain {

    public static final String FRONT = "f";
    public static final String BACK = "b";

    public static void main(String[] args) throws Exception {
        CommandLine cmd = validateCommandLine(args);
        String front = cmd.getOptionValue(FRONT);
        String back = cmd.getOptionValue(BACK);

        PDDocument frontDocument = PDDocument.load(new File(front));
        PDDocument backDocument = PDDocument.load(new File(back));

        Stack<PDPage> backPageStack = new Stack<PDPage>();
        for(PDPage page : backDocument.getPages()) {
            backPageStack.push(page);
        }

        PDDocument merged = new PDDocument();

        for(PDPage page : frontDocument.getPages()) {
            merged.addPage(page);
            if(!backPageStack.empty()) {
                merged.addPage(backPageStack.pop());
            }
        }

        merged.save("merged.pdf");
    }

    private static CommandLine validateCommandLine(String[] args) throws ParseException {
        Options options = new Options()
                .addOption(Option.builder(FRONT).longOpt("front").desc("Front pages pdf").hasArg().required().build())
                .addOption(Option.builder(BACK).longOpt("back").desc("Back pages pdf").hasArg().required().build());

        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}
