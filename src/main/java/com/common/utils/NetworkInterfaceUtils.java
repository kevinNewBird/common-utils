package com.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.stream.Collectors;

/**
 * description: com.common.utils
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/5/21
 * version: 1.0
 */
public class NetworkInterfaceUtils {

    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            System.out.println(MessageFormat.format("DisplayName: {0},Name: {1}, isVirtual: {2}, isLoopback: {3}, isUp: {4}"
                    , networkInterface.getDisplayName(), networkInterface.getName(), networkInterface.isVirtual()
                    , networkInterface.isLoopback(), networkInterface.isUp()));

            System.out.println("****************************************************************************************");
            String interfaceAddressStr = networkInterface.getInterfaceAddresses().stream().map(interfaceAddress -> {
                        InetAddress inetAddress = interfaceAddress.getAddress();
                        String s = "\nAddress::::" + convertInetAddressToString(inetAddress);
                        inetAddress = interfaceAddress.getBroadcast();
                        s += "\nBroadcast::::" + (inetAddress == null ? "" : convertInetAddressToString(inetAddress));
                        s += "\nnetworkPrefixLength::: " + interfaceAddress.getNetworkPrefixLength();

                        return s;
                    })
                    .collect(Collectors.joining(","));
            System.out.println(MessageFormat.format("InterfaceAddress : \n{0}", interfaceAddressStr));

            System.out.println("****************************************************************************************");
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                System.out.println(convertInetAddressToString(inetAddress));
            }

            System.out.println();
            System.out.println("======================================================================================");
        }
    }

    private static String convertInetAddressToString(InetAddress inetAddress) {
        return MessageFormat.format("InetAddress[hostAddress:hostName:canonicalHostName:isSiteLocalAddress] : [{0} ||| {1} ||| {2} ||| {3}]"
                , inetAddress.getHostAddress(), inetAddress.getHostName(), inetAddress.getCanonicalHostName(),inetAddress.isSiteLocalAddress());
    }
}
