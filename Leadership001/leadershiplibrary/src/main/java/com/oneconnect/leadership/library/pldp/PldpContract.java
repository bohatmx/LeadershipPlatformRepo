package com.oneconnect.leadership.library.pldp;

import com.oneconnect.leadership.library.data.PldpDTO;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by nkululekochrisskosana on 2017/11/24.
 */

public class PldpContract {

    public interface Presenter {
        void addPldp(PldpDTO pldp);
        void getCurrentUser(String email);
    }

    public interface View {
        void onPldpUploaded(String key);
        void onUserFound(UserDTO user);
        void onError(String message);

    }
}
