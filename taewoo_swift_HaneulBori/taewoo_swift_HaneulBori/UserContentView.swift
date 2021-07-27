//
//  MainContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct UserContentView: View {
    @State var state1 = "미사용"
    @State var state2 = "미사용"
    @State var btn1state = false
    @State var btn2state = false
    
    var body: some View {
        VStack {
            VStack {
                VStack {
                    Text("1번 세탁기").foregroundColor(.green)
                    if state1 == "고장"{
                        Text("\(state1)").foregroundColor(.red)
                    }
                    else{
                        Text("\(state1)").foregroundColor(.green)
                    }
                }
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
                VStack {
                    Text("2번 세탁기").foregroundColor(.blue)
                    if state2 == "고장"{
                        Text("\(state2)").foregroundColor(.red)
                    }
                    else{
                        Text("\(state2)").foregroundColor(.blue)
                    }                }
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
            }.padding()
            Spacer()
            HStack {
                Button(action: {state1 = "사용 중"}){
                    Text("1번 세탁기 예약")
                        .foregroundColor(.black)
                        .padding()
                }
                .frame(width: 180)
                .background(Color.green.opacity(0.8))
                .border(Color.black)
                .disabled(btn1state)
                Button(action: {
                    state1 = "고장"
                    btn1state = true
                }){
                    Text("1번 세탁기 고장 신고")
                        .foregroundColor(.black)
                        .padding()
                }
                .frame(width: 180)
                .background(Color.red.opacity(0.7))
                .border(Color.black)
            }
            HStack {
                Button(action: {state2 = "사용 중"}){
                    Text("2번 세탁기 예약")
                        .foregroundColor(.black)
                        .padding()
                }
                .frame(width: 180)
                .background(Color.blue.opacity(0.8))
                .border(Color.black)
                .disabled(btn2state)
                Button(action: {
                    state2 = "고장"
                    btn2state = true
                }){
                    Text("2번 세탁기 고장 신고")
                        .foregroundColor(.black)
                        .padding()
                }
                .frame(width: 180)
                .background(Color.red.opacity(0.7))
                .border(Color.black)
            }

            Spacer()
        }
    }
}

struct UserContentView_Previews: PreviewProvider {
    static var previews: some View {
        UserContentView()
    }
}
