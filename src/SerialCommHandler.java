import com.fazecast.jSerialComm.SerialPort;

public class SerialCommHandler {
    private SerialPort commPort;

    public SerialCommHandler(String portDescriptor, int baudRate) {
        commPort = SerialPort.getCommPort(portDescriptor);
        commPort.setBaudRate(baudRate);
    }

    public boolean openPort() {
        return commPort.openPort();
    }

    public void closePort() {
        commPort.closePort();
    }

    public String readData() {
        StringBuilder dataBuffer = new StringBuilder();
        try {
            while (commPort.bytesAvailable() == 0) {
                Thread.sleep(20);
            }

            byte[] readBuffer = new byte[commPort.bytesAvailable()];
            commPort.readBytes(readBuffer, readBuffer.length);
            dataBuffer.append(new String(readBuffer).trim());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBuffer.toString();
    }
}