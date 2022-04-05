package data;

import data.constants.CallMode;

import java.sql.Time;
import java.util.Calendar;

public class CallCalculator {
    private final double totalMinutesOnCall;
    private final boolean isSameNetwork;
    private final double taxation;
    private final CallMode callMode;
    private final double callCharges;
    private final double totalCharges;
    private NetworkManager networkManager;


    protected CallCalculator(CallBuilder builder) {

        this.callMode = builder.callMode;
        this.isSameNetwork = builder.isSameNetwork;
        this.taxation = builder.taxation;
        this.totalMinutesOnCall = builder.totalMinutesOnCall;
        this.totalCharges = builder.totalCharges;
        this.callCharges = builder.callCharges;
    }

    public double getTotalMinutesOnCall() {
        return totalMinutesOnCall;
    }

    public boolean isSameNetwork() {
        return isSameNetwork;
    }

    public double getTaxation() {
        return taxation;
    }

    public CallMode getHourOfCall() {
        return callMode;
    }

    public double getTotalCharges() {
        return totalCharges;
    }

    public double getCallCharges() {
        return callCharges;
    }


    public static class CallBuilder {
        private double totalMinutesOnCall;
        private final boolean isSameNetwork;
        private double taxation;
        private CallMode callMode;
        private double totalCharges;
        private double callCharges;
        private final NetworkManager networkManager;

        public CallBuilder(double totalMinutesOnCall, NetworkManager networkManager) {
            this.totalMinutesOnCall = totalMinutesOnCall;
            isSameNetwork = networkManager.isSameNetwork();
            this.networkManager=networkManager;
        }

        public CallBuilder setTotalMinutesCalled(int totalMinutesCalled) {
            this.totalMinutesOnCall = totalMinutesCalled;
            return this;
        }





        public CallBuilder calculateCallPerMinute() {
            this.callMode = networkManager.getCallMode();

            if (callMode == CallMode.DAYTIME) {
                callCharges = totalMinutesOnCall * 4;
            } else if (callMode == CallMode.NIGHTTIME) {
                callCharges = totalMinutesOnCall * 3;
            } else {
                callCharges = totalMinutesOnCall * 5;
            }
            //apply VAT
            taxation = 16 * callCharges / 100;
            totalCharges = callCharges + taxation;

            System.out.println(totalCharges);
            return this;
        }

        public CallCalculator calculateTotalCharge() {

            return new CallCalculator(this);
        }

    }
}
