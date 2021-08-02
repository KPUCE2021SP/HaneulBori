//
//  Users.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/08/03.
//

import Foundation

struct User {
    var Name: String
    var Email: String
    var Birthday: String
    var Sex: String
    var UserType: Bool
    var PhoneNumber: String
}

#if DEBUG
let testDataUsers = [
    User(Name: "김철수", Email: "test@email.com", Birthday: "20210802", Sex: "M", UserType: true, PhoneNumber: "01012345678"),
    User(Name: "김영희", Email: "qwerty@email.com", Birthday: "20020804", Sex: "F", UserType: true, PhoneNumber: "01023456789"),
    User(Name: "박철수", Email: "c0508@email.com", Birthday: "20150508", Sex: "M", UserType: false, PhoneNumber: "03115889876"),
    User(Name: "박영희", Email: "young@email.com", Birthday: "19660418", Sex: "F", UserType: true, PhoneNumber: "01145678901"),
    User(Name: "Eden Hazrd", Email: "eh10@email.com", Birthday: "19980920", Sex: "M", UserType: false, PhoneNumber: "0215880123")
]
#endif
