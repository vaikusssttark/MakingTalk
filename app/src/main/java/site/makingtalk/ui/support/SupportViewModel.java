package site.makingtalk.ui.support;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SupportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SupportViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}