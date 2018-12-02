package cn.bear2.udp;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @project:udp-sender
 * @PackageName:cn.bear2.udp
 * @Auther:pierre94
 * @CreationDate:2018-12-02 4:44 PM
 * @Description:
 * @ModificationHisotry: Who   When   What
 * -------    --------    --------------------------
 **/
public class Sender {
    @Parameter(names = "-h", description = "logServerHost", required = false, order = 0)
    private String logServerHost = "127.0.0.1";

    @Parameter(names = "-p", description = "logServerPort", required = true, order = 1)
    private int logServerPort;

    @Parameter(names = "-r", description = "permitsPerSecond", required = false, order = 2)
    private int permitsPerSecond = 100;

    @Parameter(names = "-m", description = "maxSize", required = false, order = 3)
    private int maxSize = 10000;

    @Parameter(names = "-f", description = "filePath", required = true, order = 4)
    private String file;

    @Parameter(names = "--version", description = "UDP Sender 1.0", help = true, order = 5)
    private boolean version;

    @Parameter(names = "--noLogMode", description = "noLogMode", required = false, order = 6)
    private boolean noLogMode = false;

    @Parameter(names = "--help", help = true, order = 100)
    private boolean help;

    public static void main(String[] args) throws Exception {
        Sender cli = new Sender();
        JCommander jCommander = JCommander.newBuilder().addObject(cli).build();
        jCommander.setProgramName("java -jar  udp-sender-1.0-SNAPSHOT-jar-with-dependencies.jar");
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            jCommander.usage();
            System.exit(-1);
        }
        cli.run(jCommander);
    }

    public void run(JCommander jCommander) throws IOException {
        if (help) {
            jCommander.usage();
            return;
        }
        if (version) {
            JCommander.getConsole().println("1.0");
            return;
        }

        DatagramSocket logUdpDs = new DatagramSocket();
        List<String> line = FileUtils.readLines(new File(file));
        int lineSize = line.size();
        InetSocketAddress socketAddress = new InetSocketAddress(logServerHost, logServerPort);
        RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
        DatagramPacket dp_send = new DatagramPacket("".getBytes(), "".getBytes().length, socketAddress);


        for (int i = 1; i < maxSize + 1; i++) {
            rateLimiter.acquire();
            String log = line.get((i % lineSize)) + "|#" + i + "\n";
            if ((i % (permitsPerSecond * 10) == 0) & !noLogMode) {
                System.out.printf("speed: %d msg/s ---sent: %d msg \n", permitsPerSecond, i);
            }
            dp_send.setData(log.getBytes());
            logUdpDs.send(dp_send);
        }
        logUdpDs.close();

    }

}
