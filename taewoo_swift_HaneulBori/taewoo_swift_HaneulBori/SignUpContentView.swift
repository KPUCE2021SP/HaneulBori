//
//  SignUpContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct SignUpContentView: View {
    @State var id = ""
    @State var pw = ""
    @State var pwcheck = ""
    @State var name = ""
    @State var bday = Date()
    
    var body: some View {
        NavigationView{
            VStack {
                Text("Sign Up")
                    .font(.largeTitle)
                TextField("TYPE ID HERE", text : $id)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                SecureField("TYPE PASSWORD HERE", text : $pw)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                SecureField("CHECK PASSWORD", text : $pwcheck)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                TextField("TYPE NAME HERE", text : $name)
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                DatePicker(selection: $bday, displayedComponents: .date){
                    Text("BIRTHDAY")
                }
                .padding()
//                .datePickerStyle(WheelDatePickerStyle())
                NavigationLink(destination: ContentView()
                                .navigationBarHidden(true)
                                .navigationBarBackButtonHidden(true)){
                    Text("SIGN UP")
                        .foregroundColor(Color.white)
                        .padding()
                }
                .border(Color.black)
                .background(Color.black)
            }
        }
    }
}

struct SignUpContentView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpContentView()
    }
}
