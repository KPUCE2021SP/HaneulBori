//
//  AdminContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct AdminContentView: View {
    var body: some View {
        VStack {
            VStack {
                VStack {
                    Text("1번 세탁기")
                    Text("(세탁기 상태)")
                }
                .foregroundColor(.green)
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
                VStack {
                    Text("2번 세탁기")
                    Text("(세탁기 상태)")
                }
                .foregroundColor(.blue)
                .multilineTextAlignment(.center)
                .padding()
                .border(Color.black)
            }.padding()
            Spacer()
        }
    }
}

struct AdminContentView_Previews: PreviewProvider {
    static var previews: some View {
        AdminContentView()
    }
}
