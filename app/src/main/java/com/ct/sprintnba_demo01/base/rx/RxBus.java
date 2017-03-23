

package com.ct.sprintnba_demo01.base.rx;


import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    public static final RxBus bus = new RxBus();
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());


    public static synchronized RxBus getInstance() {
        return bus;
    }


    private RxBus() {
       
    }


    public void send(Object event) {
        _bus.onNext(event);
    }


    public Observable<Object> toObserverable() {
        return _bus;
    }

    public <T> Observable<T> toObserverable(final Class<T> eventType) {
        return _bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }


    public boolean hasObservers() {
        return _bus.hasObservers();
    }


}