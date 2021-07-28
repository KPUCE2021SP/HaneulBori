//
//  SignUpContentView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/21.
//

import SwiftUI

struct SignUpContentView: View {
    @State var id = ""  // Email
    @State var pw = ""  // Password
    @State var pwcheck = "" // Type password again
    @State var name = ""    // Name
    @State var bday = Date()    // Birthday
    @State var sex = "" // Sex
    
    var body: some View {
        NavigationView{
            VStack {
                Text("Sign Up")
                    .font(.largeTitle)
                TextField("TYPE ID HERE", text : $id) // Id
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                SecureField("TYPE PASSWORD HERE", text : $pw)   // Password
                    .padding(.horizontal)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                SecureField("CHECK PASSWORD", text : $pwcheck)  // Password checking
                    .padding()
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                TextField("TYPE NAME HERE", text : $name)   // Name
                    .padding(.horizontal)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                HStack {
                    Button(action: {sex = "M"}){    // Male
                        if sex != "M"{
                            Text("MALE")
                                .foregroundColor(Color.white)
                                .padding()
                                .frame(width: 185)
                                .border(Color.black)
                                .background(Color.black)
                                .opacity(0.5)
                        }
                        else{
                            Text("MALE")
                                .foregroundColor(Color.white)
                                .padding()
                                .frame(width: 185)
                                .border(Color.black)
                                .background(Color.black)
                        }
                    }
                    Button(action: {sex = "F"}){    // Female
                        if sex != "F"{
                            Text("FEMALE")
                                .foregroundColor(Color.white)
                                .padding()
                                .frame(width: 185)
                                .border(Color.black)
                                .background(Color.black)
                                .opacity(0.5)
                        }
                        else{
                            Text("FEMALE")
                                .foregroundColor(Color.white)
                                .padding()
                                .frame(width: 185)
                                .border(Color.black)
                                .background(Color.black)
                        }
                    }
                }
                .padding()
//                Text("\(sex)")
                DatePicker(selection: $bday, displayedComponents: .date){   // Birthday
                    Text("BIRTHDAY")
                }
                .padding(.horizontal)
//                .datePickerStyle(WheelDatePickerStyle())
                NavigationLink(destination: ContentView()
                                .navigationBarHidden(true)
                                .navigationBarBackButtonHidden(true)){  // Complete signing up
                    Text("SIGN UP")
                        .foregroundColor(Color.white)
                        .padding()
                }
                .border(Color.black)
                .background(Color.black)
                Spacer()
            }
        }
    }
}

struct SignUpContentView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpContentView()
    }
}
