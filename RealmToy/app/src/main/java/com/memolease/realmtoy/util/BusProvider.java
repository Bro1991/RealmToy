package com.memolease.realmtoy.util;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by bro on 2017-08-18.
 */

//Singletone 패턴사용을 위해 추가한 otto라이브러리를 사용하기 위한 클래스 생성
public class BusProvider {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
    }
}
