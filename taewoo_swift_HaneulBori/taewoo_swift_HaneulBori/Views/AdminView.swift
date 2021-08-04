//
//  AdminContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct AdminView: View {
    @State var btnPressed = false   // For $how me the money
    @State var money = 1000000  // Earned money
    @EnvironmentObject var viewModel: AppViewModel
    
    var body: some View {
        NavigationView{
            VStack {
                VStack {
                    VStack {    // Washer No.1
                        Text("1번 세탁기")
                        Text("\(viewModel.state1)")
                    }
                    .foregroundColor(.green)
                    .multilineTextAlignment(.center)
                    .padding()
                    .border(Color.black)
                    VStack {    // Washer No.2
                        Text("2번 세탁기")
                        Text("\(viewModel.state2)")
                    }
                    .foregroundColor(.blue)
                    .multilineTextAlignment(.center)
                    .padding()
                    .border(Color.black)
                }.padding()
                Spacer()
                NavigationLink(destination: Admin_ManageView(isFixed1: !viewModel.btn1state, isFixed2: !viewModel.btn2state)
                                .navigationBarHidden(true)
                                .navigationBarBackButtonHidden(true)){   // Manage Washers
                    Text("세탁기 관리")
                        .foregroundColor(Color.white)
                        .padding()
                        .frame(width: 300)
                        .background(Color.black)
                        .border(Color.black)
                }
                Button(action: {btnPressed.toggle()}) { // For money how much you earned
                    if btnPressed{
                        Text("매출액 : \(viewModel.money) (KRW)")
                            .foregroundColor(Color.white)
                            .padding()
                    }
                    else{
                        Text("$how Me the Money")
                            .foregroundColor(Color.yellow)
                            .padding()
                    }

                }
                .frame(width: 300)
                .background(Color.black)
                .border(Color.black)
                Spacer()
//                if btnPressed{
//                    Text("I earned $\(money)")
//                }
//                Spacer()
                HStack {
                    Text("다른 계정을 사용하고 싶다면")
                    Button(action: {viewModel.signOut()}) {  // Sign out
                        Text("로그아웃")
                            .padding()
                    }
                }
            }
        }
    }
}

struct AdminView_Previews: PreviewProvider {
    static var previews: some View {
        AdminView()
    }
}
