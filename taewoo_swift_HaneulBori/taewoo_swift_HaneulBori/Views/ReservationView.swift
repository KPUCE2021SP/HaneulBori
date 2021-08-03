//
//  ReservationView.swift
//  taewoo_swift_HaneulBori
//
//  Created by 권태우 on 2021/07/28.
//

import SwiftUI

struct ReservationView: View {
    @State var time = Date()
    @State var isReserved = false
    @EnvironmentObject var viewModel: AppViewModel
    
    var body: some View {
        NavigationView{
            VStack {
                Text("세탁기 예약")
                    .font(.title)
                Spacer()
                DatePicker(selection: $time, displayedComponents: .hourAndMinute){
                    Text("예약 시간")
    //                    .frame(width: 100.0)
                }
                .padding()
                .frame(width: 380.0)
    //            .datePickerStyle(WheelDatePickerStyle())
                Button(action: {isReserved = true}){    // Female
                    if isReserved{
                        Text("예약하기")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 185)
                            .border(Color.black)
                            .background(Color.black)
                            .opacity(0.5)
                    }
                    else{
                        Text("예약하기")
                            .foregroundColor(Color.white)
                            .padding()
                            .frame(width: 185)
                            .border(Color.black)
                            .background(Color.black)
                    }
                }
                .disabled(isReserved)
                NavigationLink(destination: UserView(state1: viewModel.state1, state2: viewModel.state2, btn1state: viewModel.btn1state, btn2state: viewModel.btn2state)
                                .navigationBarHidden(true)
                                .navigationBarBackButtonHidden(true)){  // Sign up
                    Text("취소")
                        .font(.caption)
                }
                Spacer()
            }
        }
    }
}

struct ReservationView_Previews: PreviewProvider {
    static var previews: some View {
        ReservationView()
    }
}
