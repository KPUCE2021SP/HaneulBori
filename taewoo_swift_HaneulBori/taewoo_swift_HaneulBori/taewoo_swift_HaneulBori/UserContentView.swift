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
    
    var body: some View {
        VStack {
            VStack {
                VStack {
                    Text("1번 세탁기")
                    Text("\(state1)")
                }
                .foregroundColor(.green)
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
                VStack {
                    Text("2번 세탁기")
                    Text("\(state2)")
                }
                .foregroundColor(.blue)
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
            }.padding()
            Spacer()
            HStack {
                Button(action: {state1 = "사용 중"}){
                    Text("1번 세탁기 사용")
                        .foregroundColor(.black)
                        .padding()
                }
                .background(Color.green.opacity(0.8))
                .border(Color.black)
                Button(action: {state1 = "고장"}){
                    Text("1번 세탁기 고장 신고")
                        .foregroundColor(.black)
                        .padding()
                }
                .background(Color.red.opacity(0.7))
                .border(Color.black)
            }
            HStack {
                Button(action: {state2 = "사용 중"}){
                    Text("2번 세탁기 사용")
                        .foregroundColor(.black)
                        .padding()
                }
                .background(Color.blue.opacity(0.8))
                .border(Color.black)
                Button(action: {state2 = "고장"}){
                    Text("2번 세탁기 고장 신고")
                        .foregroundColor(.black)
                        .padding()
                }
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
