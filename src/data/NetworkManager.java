package data;

import data.constants.CallMode;
import data.constants.Network;

import java.sql.Time;
import java.util.Calendar;

public class NetworkManager {
    private String userPhoneNumber;
    private String receiverPhoneNumber;
    private boolean isSameNetwork;

    public NetworkManager(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public NetworkManager(String userPhoneNumber, String receiverPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
        this.receiverPhoneNumber = receiverPhoneNumber;

        if (userPhoneNumber.equals(receiverPhoneNumber)) {
            throw new IllegalStateException("User Phone Number cannot be the same as receivers");
        }
        if (getUserNetwork().equals(Network.UNKNOWN.name()) || getReceiverNetwork().equals(Network.UNKNOWN.name())) {
            throw new IllegalStateException("Unknown Network");
        }
        this.isSameNetwork = getUserNetwork().equals(getReceiverNetwork());
    }

    public String getUserNetwork() {
        return getPhoneNetwork(userPhoneNumber).name();
    }

    public String getReceiverNetwork() {
        return getPhoneNetwork(receiverPhoneNumber).name();
    }


    public boolean isNumberValid(String phoneNumber) {

        if (phoneNumber != null) {
            String nPhoneNo = phoneNumber.trim();
            char firstNumber = nPhoneNo.charAt(0);
            if (firstNumber == '7' || firstNumber == '1') {
                if (nPhoneNo.length() == 9) {
                    try {
                        int number = Integer.parseInt(nPhoneNo);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }

            }
        }
        return false;
    }

    private Network getPhoneNetwork(String phoneNumber) {
        Network network = Network.UNKNOWN;
        if (isNumberValid(phoneNumber)) {

            String first3Digits = phoneNumber.substring(0, 3);
            String first2Digits = phoneNumber.substring(0, 2);
            int networkCOde = Integer.parseInt(first3Digits);
            int newCode = Integer.parseInt(first2Digits);

            if (networkCOde >= 701 && networkCOde <= 729) {
                network = Network.SAFARICOM;
            } else if (networkCOde >= 730 && networkCOde <= 739) {
                network = Network.AIRTEL_KE;
            } else if (networkCOde >= 770 && networkCOde <= 779) {
                network = Network.TELKOM_KE;
            } else if (networkCOde >= 740 && networkCOde <= 743) {
                network = Network.SAFARICOM;
            } else if (networkCOde >= 745 && networkCOde <= 746) {
                network = Network.SAFARICOM;
            } else if (networkCOde == 748) {
                network = Network.SAFARICOM;
            } else if (networkCOde >= 750 && networkCOde <= 756) {
                network = Network.AIRTEL_KE;
            } else if (networkCOde >= 757 && networkCOde <= 759) {
                network = Network.SAFARICOM;
            } else if (networkCOde == 762) {
                network = Network.AIRTEL_KE;
            } else if (networkCOde >= 768 && networkCOde <= 769) {
                network = Network.SAFARICOM;
            } else if (networkCOde >= 780 && networkCOde <= 789) {
                network = Network.AIRTEL_KE;
            } else if (networkCOde >= 790 && networkCOde <= 799) {
                network = Network.SAFARICOM;
            } else {
                if (newCode == 10) {
                    network = Network.AIRTEL_KE;
                } else if (newCode == 11) {
                    network = Network.SAFARICOM;
                }
            }
        }

        return network;
    }

    public boolean isSameNetwork() {
        return isSameNetwork;
    }

    public void setSameNetwork(boolean sameNetwork) {
        isSameNetwork = sameNetwork;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public CallMode getCallMode() {
        CallMode userCallMode;
        Calendar calendar = Calendar.getInstance();
        Time time = new Time(calendar.getTimeInMillis());

        int hour = time.toLocalTime().getHour();

        if (hour >= 6 && hour <= 17) {
            userCallMode = CallMode.DAYTIME;
        } else {
            userCallMode = CallMode.NIGHTTIME;
        }

        if (!isSameNetwork) {
            userCallMode = CallMode.OTHER_NETWORKS;
        }
        return userCallMode;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }
}
