//
//  MainContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI
import FirebaseAuth

struct UserView: View {
    @State var state1: String   // Washer No.1 state
    @State var state2: String   // Washer No.2 state
    @State var btn1state: Bool  // Washer No.1 broken
    @State var btn2state: Bool  // Washer No.2 broken
    @EnvironmentObject var viewModel: AppViewModel
    
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
                        }
                    }
                    .multilineTextAlignment(.center)
                    .padding()
                    .border(Color.black)
                }.padding()
                Spacer()
                HStack { // Button for reservation
                    NavigationLink(destination: ReservationView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){
                        Text("세탁기 예약")
                            .foregroundColor(.white)
                            .padding()
                            .frame(width: 180, height: 100)
                            .background(Color.black.opacity(0.8))
                            .border(Color.black)
                    }
                    VStack {    // Buttons for error
                        Button(action: {
                            viewModel.state1 = "고장"
                            viewModel.btn1state = true
                            state1 = viewModel.state1
                            btn1state = viewModel.btn1state
                        }){
                            Text("1번 세탁기 고장 신고")
                                .foregroundColor(.black)
                                .padding()
                                .frame(width: 180, height: 46)
                                .background(Color.red.opacity(0.7))
                                .border(Color.black)
                        }
                        Button(action: {
                            viewModel.state2 = "고장"
                            viewModel.btn2state = true
                            state2 = viewModel.state2
                            btn2state = viewModel.btn2state
                        }){
                            Text("2번 세탁기 고장 신고")
                                .foregroundColor(.black)
                                .padding()
                                .frame(width: 180, height: 46)
                                .background(Color.red.opacity(0.7))
                                .border(Color.black)
                        }
                    }
                }
                Spacer()
                HStack {
                    Text("다른 계정을 사용하고 싶다면")
//                    NavigationLink(destination: SignInView()
//                                    .navigationBarHidden(true)
//                                    .navigationBarBackButtonHidden(true)){  // Sign up
//                        Text("로그아웃")
//                            .padding()
//                    }
                    Button(action: {
                        viewModel.signOut()
//                        SignInView()
                    }) {  // Sign out
                        Text("로그아웃")
                            .padding()
                    }
                }
            }
        }
    }
}

struct UserView_Previews: PreviewProvider {
    static var previews: some View {
        UserView(state1: "미사용", state2: "미사용", btn1state: false, btn2state: false)
    }
}
