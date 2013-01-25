
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class Main {

    private static void printAnswer(String name, Lookup lookup) throws Exception {

        System.out.print(name + ": ");
        int result = lookup.getResult();

        if (result != Lookup.SUCCESSFUL) {
            System.out.print(" " + lookup.getErrorString());
        }
        
        System.out.println();
        Name[] aliases = lookup.getAliases();

        if (aliases.length > 0) {
            System.out.print("\taliases: ---------------------->");

            for (int i = 0; i < aliases.length; i++) {
                System.out.print(aliases[i]);

                if (i < aliases.length - 1) {
                    System.out.print(" ");
                }
            }

            System.out.println();
        }

        // If the website was found
        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            // Print MX Server
            Record[] mailrec = new Lookup(name, Type.MX).run();
            for (int i = 0; i < mailrec.length; i++) {
                MXRecord mail = (MXRecord) mailrec[i];
                System.out.println("Mail server = " + mail.getTarget() 
                        + ", mail priority = " + mail.getPriority());

            }
            //Print Mail server IP address
            for (int i = 0; i < mailrec.length; i++) {
                MXRecord mailIP = (MXRecord) mailrec[i];

                InetAddress mailaddress = null;
                String printmailIP = mailIP.getTarget().toString();
                mailaddress = InetAddress.getByName(printmailIP);
                System.out.println("Mail server IP: " + mailaddress.getHostName()
                        + " - " + mailaddress.getHostAddress());
            }

            // Print name server
            Record[] nsrecords = new Lookup(name, Type.NS).run();
            for (int n = 0; n < nsrecords.length; n++) {
                NSRecord nserver = (NSRecord) nsrecords[n];
                System.out.println("Name server: " + nserver.getTarget());
            }

            //Print Name server IP address
            for (int n = 0; n < nsrecords.length; n++) {
                NSRecord nameIP = (NSRecord) nsrecords[n];

                InetAddress nameaddress = null;
                String printnameIP = nameIP.getTarget().toString();
                nameaddress = InetAddress.getByName(printnameIP);
                System.out.println("Name Server IP: " + nameaddress.getHostName() 
                        + " - " + nameaddress.getHostAddress());
            }

        }

    }

    public static void main(String[] args) throws Exception {
        
        // Record type setup
        short type = Type.A;
        int start = 0;
        
        // IO handler
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        String uinput = "";

        while (!(uinput.equals("quit")||uinput.equals("exit"))) {

            System.out.println("Enter address to lookup:");
            uinput = in.readLine();

            Lookup l = new Lookup(uinput, type);
            l.run();
            printAnswer(uinput, l);
        }
    }
}
