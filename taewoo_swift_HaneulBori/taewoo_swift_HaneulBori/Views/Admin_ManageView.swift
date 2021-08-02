//
//  Admin_ManageView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/28.
//

import SwiftUI

struct Admin_ManageView: View {
    @State var isFixed = false
    @State var state1 = false
    
    var body: some View {
        VStack {
            VStack {
                VStack {    // Washer No.1
                    Text("1번 세탁기")
                    Text("(세탁기 상태)")
                }
                .foregroundColor(.green)
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
                VStack {    // Washer No.2
                    Text("2번 세탁기")
                    Text("(세탁기 상태)")
                }
                .foregroundColor(.blue)
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
            }.padding()
            Spacer()
            Button(action: {isFixed.toggle()}) {
                if isFixed{
                    Text("이상 없음")
                        .padding()
                        .frame(width: 300)
                        .border(Color.black)
                }
                else{
                    Text("수리 필요")
                        .foregroundColor(Color.black)
                        .padding()
                        .frame(width: 300)
                        .border(Color.black)
                        .background(Color.red)
                }
            }
            .disabled(isFixed)
            Spacer()
        }
    }
}

struct Admin_ManageView_Previews: PreviewProvider {
    static var previews: some View {
        Admin_ManageView()
    }
}
