setImmediate(function () { //prevent timeout
    Java.perform(function () {
        // -------------------- ignore this setion it is for screen recorder purposes ----------
        var surface_view = Java.use('android.view.SurfaceView');
        var set_secure = surface_view.setSecure.overload('boolean');
        set_secure.implementation = function (flag) {
            set_secure.call(false);
        };
        var window = Java.use('android.view.Window');
        var set_flags = window.setFlags.overload('int', 'int');
        var window_manager = Java.use('android.view.WindowManager');
        var layout_params = Java.use('android.view.WindowManager$LayoutParams');
        set_flags.implementation = function (flags, mask) {
            flags = (flags.value & ~layout_params.FLAG_SECURE.value);
            set_flags.call(this, flags, mask);
        };


        // ----------------Staring of Spoofer------------------------------------

        const PHResponse = Java.use("lk.payhere.androidsdk.PHResponse");
        const StatusResponse = Java.use("lk.payhere.androidsdk.model.StatusResponse");
        const randomNumber = Math.floor(100000 + Math.random() * 90000);
        //  -----------creating Spoofed Instances ----------------
        let fake_SatusResponse = StatusResponse.$new();
        fake_SatusResponse.status.value = 2;
        fake_SatusResponse.paymentNo.value = randomNumber;
        fake_SatusResponse.message.value = "Payment success. Check response data";

        let fake_PHResponse = PHResponse.$new(1, fake_SatusResponse.message.value, fake_SatusResponse);

        // ----------------- Hooking method calls ---------------------------------------------------
        PHResponse["isSuccess"].implementation = function () {
            console.log(`PHResponse.isSuccess is called, Returning True`);
            return true;
        };

        PHResponse["getData"].implementation = function () {
            console.log(`PHResponse.getData is called, Returning spoofed Instance:${fake_PHResponse.getData()}`);
            return fake_PHResponse.getData();
        };
        PHResponse["toString"].implementation = function () {
            console.log(`PHResponse.toString is called, Returning spoofed Instance:${fake_PHResponse.toString()}`);
            return fake_PHResponse.toString();
        };
    });
});
