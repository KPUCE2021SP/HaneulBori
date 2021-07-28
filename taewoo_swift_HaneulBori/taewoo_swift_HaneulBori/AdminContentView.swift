//
//  AdminContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct AdminContentView: View {
    @State var btnPressed = false   // For $how me the money
    @State var money = 1000000  // Earned money
    
    var body: some View {
        NavigationView{
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
                NavigationLink(destination: SignUpContentView()){   // Manage Washers
                    Text("MANAGE WASHING MACHINES")
                        .foregroundColor(Color.white)
                        .padding()
                }
                .frame(width: 300)
                .border(Color.black)
                .background(Color.black)
                Button(action: {btnPressed.toggle()}) { // For money how much you earned
                    if btnPressed{
                        Text("I earned \(money) (KRW)")
                            .foregroundColor(Color.white)
                            .padding()
                    }
                    else{
                        Text("$how Me the Money")
                            .foregroundColor(Color.white)
                            .padding()
                    }

                }
                .frame(width: 300)
                .border(Color.black)
                .background(Color.black)
                Spacer()
//                if btnPressed{
//                    Text("I earned $\(money)")
//                }
//                Spacer()
            }
        }
    }
}

struct AdminContentView_Previews: PreviewProvider {
    static var previews: some View {
        AdminContentView()
    }
}
