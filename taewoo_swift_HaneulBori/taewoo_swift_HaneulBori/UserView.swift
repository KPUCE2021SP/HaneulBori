//
//  MainContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct UserView: View {
    @State var state1 = "미사용"   // Washer No.1 state
    @State var state2 = "미사용"   // Washer No.2 state
    @State var btn1state = false    // Washer No.1 broken
    @State var btn2state = false    // Washer No.2 broken
    
    var body: some View {
        NavigationView{
            VStack {
                VStack {    // Two washers state
                    VStack {    // Washer No.1
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
                    VStack {    // Washer No.2
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
                HStack { // Buttons for washer No.1
                    NavigationLink(destination: ReservationView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){
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
                HStack {    // Buttons for washer No.2
                    NavigationLink(destination: ReservationView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){                        Text("2번 세탁기 예약")
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
}

struct UserView_Previews: PreviewProvider {
    static var previews: some View {
        UserView()
    }
}
