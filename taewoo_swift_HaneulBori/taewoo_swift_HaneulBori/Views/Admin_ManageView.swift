//
//  Admin_ManageView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/28.
//

import SwiftUI

struct Admin_ManageView: View {
    @State var isFixed1: Bool
    @State var isFixed2: Bool
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
                Button(action: {
                    isFixed1.toggle()
                    viewModel.state1 = "미사용"
                }) {
                    HStack{
                        if isFixed1 {
                            Text("1번 세탁기 이상 없음")
                                .padding()
                                .frame(width: 300)
                                .border(Color.black)
                        }
                        else {
                            Text("1번 세탁기 수리 필요")
                                .foregroundColor(Color.black)
                                .padding()
                                .frame(width: 300)
                                .background(Color.red)
                                .border(Color.black)
                        }
                    }
                }
                .disabled(isFixed1)
                Button(action: {
                    isFixed2.toggle()
                    viewModel.state2 = "미사용"
                }) {
                    HStack{
                        if isFixed2 {
                            Text("2번 세탁기 이상 없음")
                                .padding()
                                .frame(width: 300)
                                .border(Color.black)
                        }
                        else {
                            Text("2번 세탁기 수리 필요")
                                .foregroundColor(Color.black)
                                .padding()
                                .frame(width: 300)
                                .background(Color.red)
                                .border(Color.black)
                        }
                    }
                }
                .disabled(isFixed2)
                NavigationLink(destination: AdminView()
                                .navigationBarHidden(true)
                                .navigationBarBackButtonHidden(true)){
                    Text("완료")
                        .foregroundColor(Color.white)
                        .padding()
                        .frame(width: 300)
                        .background(Color.black)
                        .border(Color.black)
                }
            }
        }
    }
}

struct Admin_ManageView_Previews: PreviewProvider {
    static var previews: some View {
        Admin_ManageView(isFixed1: true, isFixed2: true)
    }
}
