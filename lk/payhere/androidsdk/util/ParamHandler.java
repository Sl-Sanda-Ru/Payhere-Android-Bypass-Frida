package lk.payhere.androidsdk.util;

import com.facebook.appevents.integrity.IntegrityManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lk.bhasha.helakuru.Constants;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;

/* loaded from: classes7.dex */
public class ParamHandler {
    public static String convertAmount(double d) {
        return String.format(PHConstants.DEFAULT_LOCALE, "%.2f", Double.valueOf(d));
    }

    public static Map<String, String> createParams(InitRequest initRequest) throws UnsupportedEncodingException {
        HashMap hashMap = new HashMap();
        putToMap(hashMap, "merchant_id", initRequest.getMerchantId());
        putToMap(hashMap, "return_url", PHConstants.dummyUrl);
        putToMap(hashMap, "cancel_url", PHConstants.dummyUrl);
        putToMap(hashMap, "notify_url", PHConstants.dummyUrl);
        putToMap(hashMap, "first_name", initRequest.getCustomer().getFirstName());
        putToMap(hashMap, "last_name", initRequest.getCustomer().getLastName());
        putToMap(hashMap, "email", initRequest.getCustomer().getEmail());
        putToMap(hashMap, "phone", initRequest.getCustomer().getPhone());
        putToMap(hashMap, IntegrityManager.INTEGRITY_TYPE_ADDRESS, initRequest.getCustomer().getAddress().getAddress());
        putToMap(hashMap, "city", initRequest.getCustomer().getAddress().getCity());
        putToMap(hashMap, "country", initRequest.getCustomer().getAddress().getCountry());
        putToMap(hashMap, "order_id", initRequest.getOrderId());
        putToMap(hashMap, FirebaseAnalytics.Param.ITEMS, initRequest.getItemsDescription());
        putToMap(hashMap, FirebaseAnalytics.Param.CURRENCY, initRequest.getCurrency());
        putToMap(hashMap, "amount", convertAmount(initRequest.getAmount()));
        putToMap(hashMap, "delivery_address", initRequest.getCustomer().getDeliveryAddress().getAddress());
        putToMap(hashMap, "delivery_city", initRequest.getCustomer().getDeliveryAddress().getCity());
        putToMap(hashMap, "delivery_country", initRequest.getCustomer().getDeliveryAddress().getCountry());
        putToMap(hashMap, "internal_checkout", "false");
        putToMap(hashMap, Constants.PARAM_PLATFORM, "android");
        putToMap(hashMap, "custom_1", initRequest.getCustom1());
        putToMap(hashMap, "custom_2", initRequest.getCustom2());
        List<Item> items = initRequest.getItems();
        if (!items.isEmpty()) {
            int i = 0;
            while (i < items.size()) {
                Item item = items.get(i);
                i++;
                putToMap(hashMap, p1.c("item_number_", i), item.getId());
                putToMap(hashMap, p1.c("item_name_", i), item.getName());
                putToMap(hashMap, p1.c("quantity_", i), String.valueOf(item.getQuantity()));
                putToMap(hashMap, p1.c("amount_", i), String.valueOf(item.getAmount()));
            }
        }
        return hashMap;
    }

    private static void putToMap(Map<String, String> map, String str, String str2) throws UnsupportedEncodingException {
        if (str == null || str2 == null) {
            return;
        }
        map.put(str, str2);
    }
}
