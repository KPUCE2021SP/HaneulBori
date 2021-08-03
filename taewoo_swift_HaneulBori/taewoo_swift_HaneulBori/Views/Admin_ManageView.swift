//
//  Admin_ManageView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/28.
//

import SwiftUI

struct Admin_ManageView: View {
    @State var isFixed1 = false
    @State var isFixed2 = false
    @EnvironmentObject var viewModel: AppViewModel

    
    var body: some View {
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
                    Text("\(viewModel.state1)")
                }
                .foregroundColor(.blue)
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
            }.padding()
            Spacer()
            Button(action: {isFixed1.toggle()}) {
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
                            .border(Color.black)
                            .background(Color.red)
                    }
                }
            }
            .disabled(isFixed1)
            Button(action: {isFixed2.toggle()}) {
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
                            .border(Color.black)
                            .background(Color.red)
                    }
                }
            }
            .disabled(isFixed2)
            Spacer()
        }
    }
}

struct Admin_ManageView_Previews: PreviewProvider {
    static var previews: some View {
        Admin_ManageView()
    }
}
