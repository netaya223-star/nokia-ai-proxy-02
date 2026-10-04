import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;

public class ClaudeAI extends MIDlet implements CommandListener, Runnable {
    private Display display;
    private Form form;
    private TextField inputField;
    private StringItem responseItem;
    private Command sendCommand;
    private Command exitCommand;

    // הכתובת של השרת שלך ב-Render
    private String proxyUrl = "http://nokia-ai-proxy.onrender.com/ask?prompt=";

    public ClaudeAI() {
        display = Display.getDisplay(this);
        
        form = new Form("Claude AI");
        inputField = new TextField("Ask Claude:", "", 256, TextField.ANY);
        responseItem = new StringItem("Response:", "Type a question and press Send...");
        
        sendCommand = new Command("Send", Command.OK, 1);
        exitCommand = new Command("Exit", Command.EXIT, 2);
        
        form.append(inputField);
        form.append(responseItem);
        form.addCommand(sendCommand);
        form.addCommand(exitCommand);
        form.setCommandListener(this);
    }

    protected void startApp() {
        display.setCurrent(form);
    }

    protected void pauseApp() {}

    protected void destroyApp(boolean unconditional) {}

    public void commandAction(Command c, Displayable d) {
        if (c == sendCommand) {
            responseItem.setText("Thinking...");
            new Thread(this).start();
        } else if (c == exitCommand) {
            destroyApp(true);
            notifyDestroyed();
        }
    }

    public void run() {
        HttpConnection conn = null;
        InputStream is = null;
        try {
            String text = inputField.getString();
            String encodedText = replaceSpaces(text);
            String fullUrl = proxyUrl + encodedText;

            conn = (HttpConnection) Connector.open(fullUrl);
            conn.setRequestMethod(HttpConnection.GET);

            if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
                is = conn.openInputStream();
                StringBuffer sb = new StringBuffer();
                int ch;
                while ((ch = is.read()) != -1) {
                    sb.append((char) ch);
                }
                responseItem.setText(sb.toString());
            } else {
                responseItem.setText("Server Error: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            responseItem.setText("Connection Error: " + e.getMessage());
        } finally {
            try {
                if (is != null) is.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
    }

    private String replaceSpaces(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == ' ') {
                result.append("%20");
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
