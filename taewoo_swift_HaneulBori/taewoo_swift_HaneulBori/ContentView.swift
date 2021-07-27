//
//  ContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/19.
//

import SwiftUI

struct ContentView: View {
    @State var id = ""
    @State var pw = ""

    var body: some View {
        NavigationView{
            VStack {
                Text("Hello, Sign In To Use!")
                    .font(.largeTitle)
                Spacer()
                TextField("TYPE ID HERE", text : $id)
                    .padding([.top, .leading, .trailing])
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                SecureField("TYPE PASSWORD HERE", text : $pw)
                    .padding(.horizontal)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                HStack {
                    NavigationLink(destination: UserContentView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){
                        Text("SIGN IN for USER")
                            .foregroundColor(Color.white)
                            .padding()
                    }
                    .border(Color.black)
                    .background(Color.black)
                    NavigationLink(destination: AdminContentView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){
                        Text("SIGN IN for ADMIN")
                            .foregroundColor(Color.white)
                            .padding()
                    }
                    .border(Color.black)
                    .background(Color.black)
                }
                HStack {
                    Text("Don't have an account?")
                    NavigationLink(destination: SignUpContentView()
                                    .navigationBarHidden(true)
                                    .navigationBarBackButtonHidden(true)){
                        Text("SIGN UP")
                            .padding()
                    }
                }
                Spacer()
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
