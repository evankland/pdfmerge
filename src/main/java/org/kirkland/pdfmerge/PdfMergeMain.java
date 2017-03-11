package org.kirkland.pdfmerge;

import org.apache.commons.cli.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.util.Stack;

/**
 * Created by evan on 3/10/2017.
 */
public class PdfMergeMain {

    public static void main(String[] args) throws Exception {
        Options options = new Options()
                .addOption(Option.builder("f").longOpt("front").desc("Front pages pdf").hasArg().required().build())
                .addOption(Option.builder("b").longOpt("back").desc("Back pages pdf").hasArg().required().build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        String front = cmd.getOptionValue("f");
        String back = cmd.getOptionValue("b");

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

        if(!backPageStack.empty()) {
            merged.addPage(backPageStack.pop());
        }

        merged.save("merged.pdf");
    }
}
