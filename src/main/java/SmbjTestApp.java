import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import lombok.Builder;

import java.io.IOException;
import java.util.List;

@Builder
class Creds {
    String domain;
    String username;
    String password;
}


public class SmbjTestApp {
    private static String hostname = "corp.bankofamerica.com";

    public static void main(String[] args) {
        String domain = "CORP";
        String username = "administrator";
        String password = "Ad@62784";

        Creds creds = new Creds(domain, username, password);

        SMBClient client = new SMBClient();

        try {
            try1(client, creds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Session getSession(Connection connection, Creds creds) {
        return connection.authenticate(
                new com.hierynomus.smbj.auth.AuthenticationContext(creds.username, creds.password.toCharArray(), creds.domain)
        );
    }

    private static void try1(SMBClient client, Creds creds) throws IOException {
        String dfsPath = "london";
        String subPath = "finance";
        try (Connection connection = client.connect(hostname)) {
            Session session = getSession(connection, creds);

            try (DiskShare share = (DiskShare) session.connectShare(dfsPath)) {
                List<FileIdBothDirectoryInformation> list = share.list("");
                for (FileIdBothDirectoryInformation f : list) {
                    System.out.println(f.getFileName());
                }
            }
        }
    }


}
