# CSearchView
简单的SearchView

预览
![MSearchView](https://github.com/CSnowStack/CSearchView/blob/master/imgs/csearchview.gif)

使用
```java
dependencies {
   compile 'com.cq.csearchview:csearchview:1.2.0'
}
```

Rxjava里使用的话

`CSearchViewOnSubscriber`类
```java
public class CSearchViewOnSubscriber implements Observable.OnSubscribe<CharSequence> {
    final CSearchView mCSearchView;

    public CSearchViewOnSubscriber(CSearchView CSearchView) {
        mCSearchView = CSearchView;
    }

    @Override
    public void call(Subscriber<? super CharSequence> subscriber) {
        checkUiThread();
        final TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subscriber.onNext(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mCSearchView.addTextWatcher(textWatcher);

        subscriber.add(new MainThreadSubscription() {
            @Override protected void onUnsubscribe() {
                mCSearchView.removeTextWatch(textWatcher);
            }
        });
        subscriber.onNext(mCSearchView.getText());
    }
}

```
具体使用
```java
Observable.create(new CSearchViewOnSubscriber(csearchview))
...
```
>不要问我为什么`CSearchViewOnSubscriber`跟`RxBinding`里的`TextViewTextOnSubscribe`一样,抄的

[RxBinding](https://github.com/JakeWharton/RxBinding)
