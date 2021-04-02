package AwNFC;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import javax.smartcardio.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NFCRead extends TimerTask {

    FXMLLoader loader;
    String className;
    NFCTapController nfcTapController;
    empAddController empAddController;
    FirstAdminController firstAdminController;

    public NFCRead(FXMLLoader loader, String className){
        this.loader = loader;
        this.className = className;
        if(className.equals("NFCTapController"))
            nfcTapController = this.loader.getController();
        else if(className.equals("empAddController"))
            empAddController = this.loader.getController();
        else if(className.equals("FirstAdminController"))
            firstAdminController = this.loader.getController();
    }

    @Override
    public void run() {
        TerminalFactory factory = null;
        List<CardTerminal> terminals = null;
        try {
            factory = TerminalFactory.getDefault();

            terminals = factory.terminals().list();
        } catch (Exception ex) { //
            Logger.getLogger(NFCRead.class.getName()).log(Level.SEVERE,null, ex);
        }

        if (factory != null && factory.terminals() != null && terminals
                != null && terminals.size() > 0) {
            try {
                CardTerminal terminal = terminals.get(0);

                if (terminal != null) {

                    System.out.println(terminal);
                    if (terminal.isCardPresent()) {
                        System.out.println("Card");
                        String userId = getUserId(terminal);
                        if(className.equals("NFCTapController")) {
                            nfcTapController.setUID(userId);
                            cancel();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    nfcTapController.switchScene();
                                }
                            });
                        }else if(className.equals("empAddController")){
                            empAddController.enterEmpSerNum(userId);
                            //cancel();
                        }else if(className.equals("FirstAdminController")){
                            firstAdminController.enterEmpSerNum(userId);
                            //cancel();
                        }
                    } else {
                        System.out.println("No Card");
                        if(className.equals("NFCTapController"))
                            nfcTapController.setUID(null);
                    }

                }else {
                    System.out.println("No terminal");
                }

                terminal = null;

            } catch (Exception e) {
                Logger.getLogger(NFCRead.class.getName()).log(Level.SEVERE,null, e);
            }
            factory = null;

            terminals = null;

            Runtime.getRuntime().gc();

        } else {
            System.out.println("No terminal");
        }
    }

    public String getUserId(CardTerminal terminal) throws Exception{
        String UID = null;
        Card card = terminal.connect("*");
        //card.beginExclusive();
        CardChannel channel = card.getBasicChannel();

        // Start with something simple, read UID, kinda like Hello
        // World!
        byte[] baReadUID = new byte[5];

        baReadUID = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00,
                (byte) 0x00, (byte) 0x00 };

        UID = send(baReadUID, channel).split("9000")[0];
        System.out.println("UID: " + UID);
        // If successful, the output will end with 9000
        return UID;
    }

    public String send(byte[] cmd, CardChannel channel) {

        String res = "";

        byte[] baResp = new byte[258];
        ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
        ByteBuffer bufResp = ByteBuffer.wrap(baResp);

        // output = The length of the received response APDU
        int output = 0;

        try {

            output = channel.transmit(bufCmd, bufResp);

        } catch (CardException ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < output; i++) {
            res += String.format("%02X", baResp[i]);
            // The result is formatted as a hexadecimal integer
        }

        return res;
    }


}
