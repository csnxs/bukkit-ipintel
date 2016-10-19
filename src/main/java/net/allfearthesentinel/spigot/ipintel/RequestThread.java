package net.allfearthesentinel.spigot.ipintel;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * Created by Sean on 19/10/2016.
 */
public class RequestThread implements Runnable {
    public final Player player;

    public RequestThread(final Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        final Logger logger = Plugin.instance.logger;

        try {
            final Configuration config = Plugin.instance.config;
            final String ip = player.getAddress().getHostString();
            final String contactEmail = config.getString("contact_email");
            final double minResult = config.getDouble("min_result");

            StringBuilder paramBuilder = new StringBuilder();
            paramBuilder.append("ip=" + ip);
            paramBuilder.append("&contact=" + URLEncoder.encode(contactEmail, "UTF-8"));

            if (minResult == 1.0) {
                paramBuilder.append("&flags=m");
            }

            URL url = new URL("http://check.getipintel.net/check.php?" + paramBuilder.toString());

            HttpURLConnection connection;

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer data = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                data.append(inputLine);
            }
            in.close();

            String responseText = data.toString();
            final double response = Double.parseDouble(responseText);

            if (response < 0) {
                logger.warning("IPIntel returned " + response + "!");
                return;
            }

            if (response >= minResult) {
                final BanList banList = Bukkit.getBanList(BanList.Type.IP);
                final String reason = config.getString("ban_reason");
                banList.addBan(
                        ip, reason, null, "IPIntel"
                );
                Plugin.instance.playersToKick.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }
}
